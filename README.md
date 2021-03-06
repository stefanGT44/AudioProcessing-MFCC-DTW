# AudioProcessing-MFCC-DTW
This is a JavaFX desktop application for computing MFCCs (Mel frequency cepstrum coefficients) of input words, and finding the most similar sounding words from the database using DTW (dynamic time warp).

## Overview
The application supports .wav files as input, the user can also record sounds within the application. After the MFCCs of the input audio are computed, the values can be stored in the database or just be used for analysis - finding the most similar sounding words from the database using DTW. Saved audio files can be played, renamed and deleted within the application. During audio playback the application also provides a [spectrum visualizer](https://github.com/stefanGT44/AudioVisualizer-RealTime-Histogram/blob/master/README.md).

![Alt text](images/mfcc.png?raw=true "")
![Alt text](images/mfcc2.png?raw=true "")

## Implementation details<br>
### 1. End pointing
Determining which 10ms windows of audio contain speech.
#### 1. Calculating noise
It is assumed that the first 100ms of input audio doesn't contain speech. Noise is computed by taking the average energy of samples within the 100ms window and adding one standard deviation.
#### 2. Roughly detirmining speech windows
After calculating the noise boundary, the rest of the audio signal is analysed by 10ms windows. Using the noise boundary it is determined if a windows contains speech or not. If the average energy of samples in a window is higher than the noise boundary, the window contains speech. The result of this step is an array of 0s and 1s, where 1 represents a window containing speech and 0 otherwise.
#### 3. Conservative Z smoothing
Because certain spikes of discontinued 1s or 0s can appear in the array, smoothing is applied. For a digit to maintain it's value, it is required that there is a sequence of digits with the same value, or they get converted. The order of conversion (0->1 and 1->0) is important (results vary based on order). Conservative means that the order is 0->1, 1->0 - maintaining more information. After a conversion of 0s to 1s, if the length of the result sequence of 1s is < Z, the sequency is set to 0s.
#### 4. ZCT (zero-crossing rate) filtering
The zero-crossing rate is the rate of sign-changes along a signal within a certain time period, i.e., the rate at which the signal changes from positive to zero to negative or from negative to zero to positive. Some characteristics of speech tend to abruptly increase the ZCT, this is used to detect speech where it hasn't been detected using the noise boundary. 
### 2. Computing MFCCs
MFCCs (Mel frequency cepstrum coefficients) are commonly used as features in speech recognition, 
### 1. Calculating the Mel filter bank

### 3. DTW


## Sidenote
This project was an assignment as part of the course - Speech recognition during the 5th semester at the Faculty of Computer Science in Belgrade. All app functionalities were defined in the assignment specifications.

## Download
You can download the .jar files [here](downloads/MFCC.zip).<br>

## Contributors
- Stefan Ginic - <stefangwars@gmail.com>
