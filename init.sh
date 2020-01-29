#!/usr/bin/env bash

################## FOR UBUNTU LINUX ONLY #########################

# Run this script before running hotkey_server
# It,
# - installs necessary dependencies
# - applies fixes for commonly faced issues
# (tested on Ubuntu 18 and 19)


# install necessary dependencies
sudo apt install libxdo-dev # xdo for media key emulation
sudo apt install libbluetooth-dev # bluecove for bluetooth

# To prevent "Can not open SDP session. [2] No such file or directory" error
# see:
# https://stackoverflow.com/questions/30946821/bluecove-with-bluez-chucks-can-not-open-sdp-session-2-no-such-file-or-direct
# https://askubuntu.com/questions/775303/unified-remote-bluetooth-could-not-connect-to-sdp

# start the bluetooth daemon with the '-C' (or '--compat') option
sudo sed -ie "s|^ExecStart=/usr/lib/bluetooth/bluetoothd$|& --compat\nExecStartPost=/bin/chmod 777 /var/run/sdp|" /etc/systemd/system/bluetooth.target.wants/bluetooth.service
sudo sed -ie "s|^ExecStart=/usr/lib/bluetooth/bluetoothd$|& --compat\nExecStartPost=/bin/chmod 777 /var/run/sdp|" /etc/systemd/system/dbus-org.bluez.service
sudo systemctl daemon-reload # restart system daemons
sudo systemctl restart bluetooth # restart bluetooth service