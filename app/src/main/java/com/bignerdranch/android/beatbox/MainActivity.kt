package com.bignerdranch.android.beatbox

import android.app.Activity
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding
import com.bignerdranch.android.beatbox.databinding.ListItemSoundBinding

private const val TAG = "MAINACTIVATE"

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

	private lateinit var beatBox: BeatBox
	private lateinit var seekBar: SeekBar
	private lateinit var speedLabel: TextView


	private val beatBoxViewModel: BeatBoxViewModel by lazy {
		ViewModelProvider(this).get(BeatBoxViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (beatBoxViewModel.beatBox == null) {
			beatBoxViewModel.beatBox = BeatBox(assets)
		}
		val binding: ActivityMainBinding =
				DataBindingUtil.setContentView(this, R.layout.activity_main)

		seekBar = binding.seekBar
		seekBar.visibility = View.GONE
		seekBar.setOnSeekBarChangeListener(this)

		speedLabel = binding.playbackSpeedLabel
		speedLabel.visibility = View.GONE
		speedLabel.text = getString(R.string.playback_speed, seekBar.progress)


		binding.recyclerView.apply {
			layoutManager = GridLayoutManager(context, 3)
			adapter = SoundAdapter(beatBoxViewModel.beatBox!!.sounds)
		}
	}

	override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
		speedLabel.text = getString(R.string.playback_speed, seekBar?.progress)
		beatBoxViewModel.beatBox!!.rate = (1.0 + seekBar!!.progress / 100.0).toFloat()

	}

	override fun onStartTrackingTouch(seekBar: SeekBar?) {
	}

	override fun onStopTrackingTouch(seekBar: SeekBar?) {
		beatBoxViewModel.beatBox!!.rate = (1.0 + seekBar!!.progress / 100.0).toFloat()
	}

	override fun onDestroy() {
		super.onDestroy()
//		beatBoxViewModel.beatBox!!.release()
	}


	private inner class SoundHolder(private val binding: ListItemSoundBinding):
			RecyclerView.ViewHolder(binding.root) {

		init {
			binding.viewModel = SoundViewModel(beatBoxViewModel.beatBox!!)
		}

		fun bind(sound: Sound) {
			binding.apply {
				viewModel?.sound = sound
				executePendingBindings()
			}
		}
	}


	private inner class SoundAdapter(private val sounds: List<Sound>):
			RecyclerView.Adapter<SoundHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
			val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
					layoutInflater, R.layout.list_item_sound, parent, false
			)
			return SoundHolder(binding)
		}

		override fun onBindViewHolder(holder: SoundHolder, position: Int) {
			val sound = sounds[position]
			holder.bind(sound)
		}

		override fun getItemCount() = sounds.size

	}
}
