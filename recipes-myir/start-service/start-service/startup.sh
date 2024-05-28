#!/bin/sh
echo "start service" > /var/startservice.log

vimrc_file="/usr/share/vim/vimrc"
commented_backup=$(grep -E '^\s*".*set backup' "$vimrc_file")
commented_else=$(grep -E '^\s*".*else' "$vimrc_file")

if [[ -z $commented_backup ]]; then
	sed -i '/^\s*set backup/s/^/"/' "$vimrc_file"
	if [[ -z $commented_else ]]; then
		sed -i '/^\s*else/s/^/"/' "$vimrc_file"
	fi
	echo "Lines commented out."
else
	echo "Lines already commented out."
fi

echo "Start Up"

FILENAME="/etc/myir-hmi"

if [ ! -f "$FILENAME" ] || [ "$(cat $FILENAME)" = "0" ]; then
    echo "driftfile /var/lib/ntp/drift" > /etc/ntp.conf
    echo "server ntp.aliyun.com iburst" >> /etc/ntp.conf
    ts_calibrate
    echo "1" > $FILENAME
fi

systemctl restart ntpd

exit 0
