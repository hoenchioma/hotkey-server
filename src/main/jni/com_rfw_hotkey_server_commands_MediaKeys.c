
#include "com_rfw_hotkey_server_commands_MediaKeys.h"
#include <jni.h>
#include <stdio.h>

#if defined(_WIN32) || defined(WIN32) // windows implementation

#define WINVER 0x0500
#include <windows.h>

// hits the volume mute/unmute key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeMute(JNIEnv *env,
                                                          jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_VOLUME_MUTE;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

// hits the volume down key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeDown(JNIEnv *env,
                                                          jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_VOLUME_DOWN;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

// hits the volume up key
JNIEXPORT void JNICALL Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeUp(
    JNIEnv *env, jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_VOLUME_UP;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

// hits the previous track key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_songPrevious(JNIEnv *env,
                                                            jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_MEDIA_PREV_TRACK;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

// hits the next track key
JNIEXPORT void JNICALL Java_com_rfw_hotkey_1server_commands_MediaKeys_songNext(
    JNIEnv *env, jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_MEDIA_NEXT_TRACK;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

// hits the play/pause key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_songPlayPause(JNIEnv *env,
                                                             jobject thisObj) {

  KEYBDINPUT kbi;

  // specific keycode
  kbi.wVk = VK_MEDIA_PLAY_PAUSE;

  kbi.wScan = 0;
  kbi.dwFlags = 0;
  kbi.time = 0;
  kbi.dwExtraInfo = (ULONG_PTR)GetMessageExtraInfo();

  INPUT input;
  input.type = INPUT_KEYBOARD;
  input.ki = kbi;

  SendInput(1, &input, sizeof(INPUT));

  return;
}

#elif __linux__ // for linux systems

#include <xdo.h>

jint JNI_OnLoad(JavaVM* aVm, void* aReserved)
{
 return JNI_VERSION_1_8;
}

// hits the volume mute/unmute key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeMute(JNIEnv *env,
                                                          jobject thisObj) {

  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioMute", 0);
  return;
}

// hits the volume down key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeDown(JNIEnv *env,
                                                          jobject thisObj) {

  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioLowerVolume", 0);

  return;
}

// hits the volume up key
JNIEXPORT void JNICALL Java_com_rfw_hotkey_1server_commands_MediaKeys_volumeUp(
    JNIEnv *env, jobject thisObj) {

  
  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioRaiseVolume", 0);

  return;
}

// hits the previous track key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_songPrevious(JNIEnv *env,
                                                            jobject thisObj) {

  
  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioRaiseVolume", 0);

  return;
}

// hits the next track key
JNIEXPORT void JNICALL Java_com_rfw_hotkey_1server_commands_MediaKeys_songNext(
    JNIEnv *env, jobject thisObj) {

  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioRaiseVolume", 0);
  return;
}

// hits the play/pause key
JNIEXPORT void JNICALL
Java_com_rfw_hotkey_1server_commands_MediaKeys_songPlayPause(JNIEnv *env,
                                                             jobject thisObj) {

  
  xdo_send_keysequence_window(xdo_new(NULL), CURRENTWINDOW, "XF86AudioPlay", 0);

  return;
}

#else

#error "Unsupported OS"

#endif