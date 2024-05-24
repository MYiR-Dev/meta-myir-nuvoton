#!/bin/sh
FILENAME="/etc/myir-hmi"

if [ ! -f "$FILENAME" ] || [ "$(cat $FILENAME)" = "0" ]; then
    echo "driftfile /var/lib/ntp/drift" > /etc/ntp.conf
    echo "server ntp.aliyun.com iburst" >> /etc/ntp.conf
    ts_calibrate
    echo "1" > $FILENAME
fi
export TSLIB_CONSOLEDEVICE=none
export QT_QPA_FB_TSLIB=1
export QT_WAYLAND_SHELL_INTEGRATION=xdg-shell
export QTWEBENGINE_DISABLE_SANDBOX=1
export QT_QPA_EGLFS_ALWAYS_SET_MODE=1
export WAYLAND_DISPLAY=/run/wayland-0
export XDG_RUNTIME_DIR=/run/user/0
export  QT_QPA_PLATFORM=linuxfb:fb=/dev/fb0:offset=0x0
systemctl restart ntpd
/home/root/mxapp2
