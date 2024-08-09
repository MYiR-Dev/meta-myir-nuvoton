#!/bin/sh

#****************************************
#  copyright (C) 2014 all rights reserved
#  @file: burn_flash_emmc.sh
#  @Created: 2024-02-21 10:23
#  @Author: CrackXue
#****************************************
TOTAL_LENGTH=50

FAIL=0
echo $FAIL > /home/root/fail.txt

flag=$1
# if [[ X${flag} = X"plan" ]];then
# plan="{\"step\":\"firmware\",\"PN\":\"xxx\",\"SN\":\"xxx\",\"CN\":\"xxx\",\"plan\":{\"num\":\"3\",\"modules\":{\"boot\":\"0\", \"image\":\"2000\",\"rootfs\":\"40000\"}}}"
# echo ">>>[${#plan}]${plan}"
# exit 1
# fi

imagedir=/home/root/lma35_images
. ${imagedir}/Manifest

# eMMC  --> mmcblk1
DRIVE=/dev/mmcblk1

imagepart=""

MBR_bin_FILE=${imagedir}/${MBRfile}
header_bin_FILE=${imagedir}/${headerfile}
bl2_dtb_FILE=${imagedir}/${bl2dtbfile}
bl2_bin_FILE=${imagedir}/${bl2binfile}
env_bin_FILE=${imagedir}/${envfile}
fip_bin_FILE=${imagedir}/${fipfile}
DTBS_FILE=${imagedir}/${dtbfile}
IMAGE_FILE=${imagedir}/${imagefile}
ROOTFS_FILE=${imagedir}/${rootfsfile}



draw_progress_bar() {
    local current=$1
    local progress=$((${current}*${TOTAL_LENGTH}/100))

    printf "["
    for (( p=0; p<$TOTAL_LENGTH; p++ )); do
        if [ $p -lt $progress ]; then
            printf "="
        else
            printf " "
        fi
    done
    printf "] %d%%" $current
}

updating()
{
	echo timer > /sys/class/leds/LED-Blue/trigger
	echo 500 > /sys/class/leds/LED-Blue/delay_off
	echo 500 > /sys/class/leds/LED-Blue/delay_on	
#	time=1
#	while true;do
#		echo "[${time}]Updating..."
#		sleep 1
#		time=$((time + 1))
#	done
}

update_success()
{
	echo none > /sys/class/leds/LED-Blue/trigger
	echo 1 > /sys/class/leds/LED-Blue/brightness
	# while true;do
	# 	echo "Update complete..."
	# 	sleep 2
	# 	echo "Update complete..."
	# 	sleep 3
	# done
	# kill ${pid}
	echo -e "\n\033[1m\033[32m" > /dev/ttyS0
	echo "    ____  __  ______  _   __   _____ __  ______________________________ ____    " > /dev/ttyS0
	echo "   / __ )/ / / / __ \/ | / /  / ___// / / / ____/ ____/ ____/ ___/ ___// / /      " > /dev/ttyS0
	echo "  / __  / / / / /_/ /  |/ /   \__ \/ / / / /   / /   / __/  \__ \\__ \/ / /       " > /dev/ttyS0
	echo " / /_/ / /_/ / _, _/ /|  /   ___/ / /_/ / /___/ /___/ /___ ___/ /__/ /_/_/        " > /dev/ttyS0
	echo "/_____/\____/_/ |_/_/ |_/   /____/\____/\____/\____/_____//____/____(_|_)         " > /dev/ttyS0
    echo "                                                                                  " > /dev/ttyS0
	echo "                                           " > /dev/ttyS0
	echo " Burning process completed successfully!   " > /dev/ttyS0
	echo -e "\033[0m" > /dev/ttyS0
	echo "---------------------------success---------------------------" > /dev/ttyS0
	echo "---------------------------success---------------------------" > /dev/ttyS0
	echo "---------------------------success---------------------------" > /dev/ttyS0
}

update_fail()
{
	echo none > /sys/class/leds/LED-Blue/trigger
        echo 0 > /sys/class/leds/LED-Blue/brightness
	while true;do
		echo "Update failed..." > /dev/ttyS0
		sleep 2
		echo "Update failed..." > /dev/ttyS0
		sleep 2
	done
}

cmd_check()
{
	if [ $1 -ne 0 ];then
		echo "$2 failed!" > /dev/ttyS0
		echo
		FAIL=1
		echo $FAIL > /home/root/fail.txt
		update_fail
	fi
}

# Erasing eMMC
erasing_emmc()
{
#	echo -e "\n== Destroying Master Boot Record (sector 0) ==" > /dev/null 2>&1
	sleep 1> /dev/null 2>&1
	echo dd if=/dev/zero of=${DRIVE} bs=1M count=10 > /dev/null 2>&1
	dd if=/dev/zero of=${DRIVE} bs=1M count=10 > /dev/null 2>&1
	sync
}

fidsk_emmc(){
#Delete the old partition table
fdisk ${DRIVE} << EOF
o
w
EOF

#Creating new partitions
fdisk ${DRIVE} << EOF
n
p
1


w
EOF
cmd_check $? "Re-partition device"

umount ${DRIVE}p1 > /dev/null 2>&1
sleep 3

mkfs.ext4 -F -L "rootfs" ${DRIVE}p1 > /dev/null 2>&1
cmd_check $? "Formating rootfs partition"
imagepart=`basename ${DRIVE}p1`
}

burn_boot()
{
    #burn MBR_bin file
	dd if=${MBR_bin_FILE} of=${DRIVE} bs=512 seek=0
	cmd_check $? "Update MBR_bin file"

    #burn header_bin file
	dd if=${header_bin_FILE} of=${DRIVE} bs=512 seek=2
	cmd_check $? "Update header_bin file"

    #burn bl2_dtb file
	dd if=${bl2_dtb_FILE} of=${DRIVE} bs=512 seek=256
	cmd_check $? "Update bl2_dtb file"

    #burn bl2_bin file
	dd if=${bl2_bin_FILE} of=${DRIVE} bs=512 seek=384
	cmd_check $? "Update bl2_bin file"

    #burn env_bin file
	dd if=${env_bin_FILE} of=${DRIVE} bs=512 seek=512
	cmd_check $? "Update env_bin file"

    #burn fip_bin file
	dd if=${fip_bin_FILE} of=${DRIVE} bs=512 seek=1536
	cmd_check $? "Update bl2_dtb file"

    sleep 1
    sync
}

burn_image()
{
    #burn dtb file
    dd if=${DTBS_FILE} of=${DRIVE} bs=512 seek=5632
	cmd_check $? "Update dtb file"

    #burn Image file
    dd if=${IMAGE_FILE} of=${DRIVE} bs=512 seek=6144
    cmd_check $? "Update Image file"

    sleep 1
	sync
}

burn_rootfs()
{
    #burn rootfs file
	dd if=${ROOTFS_FILE} of=${DRIVE} bs=512 seek=65536
	cmd_check $? "Update rootfs"

	sync
}

resize2fs_emmc()
{
e2fsck -f  ${DRIVE}p1 << EOF
y
EOF
resize2fs   ${DRIVE}p1 > /dev/null 2>&1
  	
sync
}

updating
# pid=$!
clear


echo "---------------------------start erasing_emmc---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
erasing_emmc  > /home/root/log.txt &
progress_flag="$(ps | awk '{print $1}' | grep $!)"
for i in {1..99}; do
	if [ 1 -eq $(cat /home/root/fail.txt) ];then
	exit 0
	fi
	if [ -z $(ps | awk '{print $1}' | grep $progress_flag) ];then
	break
	fi
    draw_progress_bar $i > /dev/ttyS0
    sleep 0.1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end erasing_emmc---------------------------" > /dev/ttyS0
clear
echo " " > /dev/ttyS0
echo "---------------------------start fidsk_emmc---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
fidsk_emmc &
progress_flag="$(ps | awk '{print $1}' | grep $!)"
for i in {1..99}; do
	if [ 1 -eq $(cat /home/root/fail.txt) ];then
	exit 0
	fi
	if [ -z $(ps | awk '{print $1}' | grep $progress_flag) ];then
	break
	fi
    draw_progress_bar $i > /dev/ttyS0
    sleep 0.1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end fidsk_emmc---------------------------" > /dev/ttyS0
clear
echo " " > /dev/ttyS0
echo "---------------------------start burn_boot---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
burn_boot &
progress_flag="$(ps | awk '{print $1}' | grep $!)"
for i in {1..99}; do
	if [ 1 -eq $(cat /home/root/fail.txt) ];then
	exit 0
	fi
	if [ -z $(ps | awk '{print $1}' | grep $progress_flag) ];then
	break
	fi
    draw_progress_bar $i > /dev/ttyS0
    sleep 0.1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end burn_boot---------------------------" > /dev/ttyS0
clear
echo " " > /dev/ttyS0
#boot_result="{\"step\":\"firmware\",\"PN\":\"xxx\",\"SN\":\"xxx\",\"CN\":\"xxx\",\"result\":{\"module\":\"boot\",\"progress\":\"20\", \"state\":\"0\",\"message\":\"write boot succuess\"}}"
#echo ">>>[${#boot_result}]${boot_result}"

echo "---------------------------start burn_image---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
burn_image &
progress_flag="$(ps | awk '{print $1}' | grep $!)"
for i in {1..99}; do
	if [ 1 -eq $(cat /home/root/fail.txt) ];then
	exit 0
	fi
	if [ -z $(ps | awk '{print $1}' | grep $progress_flag) ];then
	break
	fi
    draw_progress_bar $i > /dev/ttyS0
    sleep 0.1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end burn_image---------------------------" > /dev/ttyS0
clear
#image_result="{\"step\":\"firmware\",\"PN\":\"xxx\",\"SN\":\"xxx\",\"CN\":\"xxx\",\"result\":{\"module\":\"image\",\"progress\":\"20\", \"state\":\"0\",\"message\":\"write image succuess\"}}"
#echo ">>>[${#image_result}]${image_result}"
echo " " > /dev/ttyS0
echo "---------------------------start burn_rootfs---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
burn_rootfs &
progress_flag="$(ps | awk '{print $1}' | grep $!)"
for i in {1..99}; do
	if [ 1 -eq $(cat /home/root/fail.txt) ];then
	exit 0
	fi
	if [ -z $(ps | awk '{print $1}' | grep $progress_flag) ];then
	break
	fi
    draw_progress_bar $i > /dev/ttyS0
    sleep 1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end burn_rootfs---------------------------" > /dev/ttyS0
clear
echo " " > /dev/ttyS0
#rootfs_result="{\"step\":\"firmware\",\"PN\":\"xxx\",\"SN\":\"xxx\",\"CN\":\"xxx\",\"result\":{\"module\":\"rootfs\",\"progress\":\"20\", \"state\":\"0\",\"message\":\"write rootfs succuess\"}}"
#echo ">>>[${#rootfs_result}]${rootfs_result}"

resize2fs_emmc

update_success
