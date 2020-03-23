# AudioProcessing-MFCC-DTW
This is a JavaFX desktop application for computing MFCCs (Mel frequency cepstrum coefficients) of input words, and finding the most similar sounding words from the database using DTW (dynamic time warp).

## Overview
The application supports .wav files as input, the user can also record sounds within the application. After the MFCCs of the input audio are computed, the values can be stored in the database or just be used for analysis - finding the most similar sounding words from the database using DTW. Saved audio files can be played, renamed and deleted within the application. During audio playback the application also provides a [spectrum visualizer](https://github.com/stefanGT44/AudioVisualizer-RealTime-Histogram/blob/master/README.md).

![Alt text](images/mfcc.png?raw=true "")
![Alt text](images/mfcc2.png?raw=true "")

## Implementation details<br>
### 1. End pointing
Determining which 10ms windows of audio represent speech and which do not.
#### * Calculating noise
It is assumed that the first 100ms of input audio don't contain speech. Noise is computed by taking the average energy of samples within the 100ms window and adding one standard deviation.
#### * Roughly detirmining speech windows
After calculating the noise boundary, the rest of the audio signal is analysed by 10ms windows. Using the noise boundary it is determined if a windows contains speech or not. If the average energy of samples in a window is higher than the noise boundary, the window contains speech. The result of this step is an array of 0s and 1s, where 1 represents a window containing speech and 0 otherwise.
#### * Conservative Z smoothing
Because certain spikes of discontinued 1s or 0s can appear in the array, smoothing is applied. For a digit to maintain it's value, it is required that there are a certain amount of digits with the same value in a row or they are converted into the opposite value. The order of conversion (0->1 and 1->0) is important (results vary based on order). Conservative means that the order is 0->1, 1->0 - maintaining more information. After conversions, if a sequence of 1s that is shorter than Z appears (a length parameter), digits in the sequence are converted to 0.
#### * ZCT (zero-crossing rate) filtering

### 2. Computing MFCCs

### 3. DTW


## Sidenote
This project was an assignment as part of the course - Speech recognition at the Faculty of Computer Science in Belgrade. All app functionalities were defined in the assignment specifications.

## Download
You can download the .jar files [here](downloads/MFCC.zip).<br>

## Contributors
- Stefan Ginic - <stefangwars@gmail.com>
