package com.bignerdranch.android.beatbox

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel


private const val TAG = "BeatBox"

class BeatBoxViewModel() : ViewModel() {
	var  beatBox: BeatBox? = null

	override fun onCleared() {
		super.onCleared()
		beatBox?.release()
	}
}
