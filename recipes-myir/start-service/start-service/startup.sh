#!/bin/sh

TIMESYNCD_CONF="/etc/systemd/timesyncd.conf"
NEW_CONF_CONTENT="[Time]\nFallbackNTP=ntp.ntsc.ac.cn cn.ntp.org.cn time1.google.com time2.google.com time3.google.com time4.google.com"

# 检查是否存在 /etc/systemd/timesyncd.conf 文件
if [ -f "$TIMESYNCD_CONF" ]; then
    # 检查文件中是否包含 #MYiR timesyncd conf 内容
    if grep -q "#MYiR timesyncd conf" "$TIMESYNCD_CONF"; then
        echo "Already configured, nothing to do."
        exit 0
    fi

    # 备份原始文件
    cp "$TIMESYNCD_CONF" "$TIMESYNCD_CONF.bak"

    # 替换文件内容为新配置
    sed -i '/\[Time\]/,$d' "$TIMESYNCD_CONF"
    echo -e "#MYiR timesyncd conf\n$NEW_CONF_CONTENT" >> "$TIMESYNCD_CONF"

    echo "Modified $TIMESYNCD_CONF with custom configuration."
else
    echo "$TIMESYNCD_CONF not found. Cannot configure timesyncd."
    exit 1
fi

echo "start service" > /var/startservice.log

echo "Start service" > /dev/ttyS0
exit 0
