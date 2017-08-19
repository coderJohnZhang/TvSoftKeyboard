# TvSoftKeyboard

[![](https://jitpack.io/v/coderJohnZhang/TvSoftKeyboard.svg)](https://jitpack.io/#coderJohnZhang/TvSoftKeyboard)

## Introduction

Android TV SoftKeyboard Library

## Features

- Develop for TV Input method 

- Multi language keyboard, support English, Arabic, Farsi, Turkish, German, French, Spanish, Italian, Russian and Chinese 

- Colorful layout

- Powerful function

## Screenshot

English Lower Case Keyboard<br><br>
<img src="https://github.com/coderJohnZhang/TvSoftKeyboard/blob/master/art/english_keyboard_lower.png" width="600"><br><br><br>

English Upper Case Keyboard<br><br>
<img src="https://github.com/coderJohnZhang/TvSoftKeyboard/blob/master/art/english_keyboard_upper.png" width="600"><br><br><br>

Arabic Keyboard<br><br>
<img src="https://github.com/coderJohnZhang/TvSoftKeyboard/blob/master/art/arabic_keyboard.png" width="600"><br><br><br>

Russian Lower Case Keyboard<br><br>
<img src="https://github.com/coderJohnZhang/TvSoftKeyboard/blob/master/art/russian_keyboard_lower.png" width="600"><br><br><br>

Russian Upper Case Keyboard<br><br>
<img src="https://github.com/coderJohnZhang/TvSoftKeyboard/blob/master/art/russian_keyboard_upper.png" width="600"><br><br><br>

## How to use

Step 1. Add the JitPack repository to your build file

### gradle
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
### maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
Step 2. Add the dependency

### gradle

	dependencies {
	        compile 'com.github.coderJohnZhang:TvSoftKeyboard:v1.0.0'
	}
	
### maven

	<dependency>
	    <groupId>com.github.coderJohnZhang</groupId>
	    <artifactId>TvSoftKeyboard</artifactId>
	    <version>v1.0.0</version>
	</dependency>

Step 3. Add databinding support

Add it in your module build.gradle

	android {  
	    ...  
	    ...  
	    ...  
	    dataBinding{  
		enabled true  
	    }  
	} 

## About me

Email: coder.john.cheung@gmail.com<br><br>
CSDN blog: http://blog.csdn.net/johnwcheung<br><br>
Github: https://github.com/coderJohnZhang

## License

		Copyright 2017 John Cheung

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

			http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
		limitations under the License.

