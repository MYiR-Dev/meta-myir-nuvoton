#!/bin/sh

#****************************************
#  copyright (C) 2014 all rights reserved
#  @file: burn_flash_spinand.sh
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


header_bin_FILE=${imagedir}/${headerfile}
bl2_dtb_FILE=${imagedir}/${bl2dtbfile}
bl2_bin_FILE=${imagedir}/${bl2binfile}
env_bin_FILE=${imagedir}/${envfile}
fip_bin_FILE=${imagedir}/${fipfile}
DTBS_FILE=${imagedir}/${dtbfile}
IMAGE_FILE=${imagedir}/${imagefile}
ROOTFS_FILE=${imagedir}/${rootfsfile}

len_header_bin_FILE=`ls -l ${header_bin_FILE} | awk -F " " '{print $5}'`
len_bl2_dtb_FILE=`ls -l ${bl2_dtb_FILE} | awk -F " " '{print $5}'`
len_bl2_bin_FILE=`ls -l ${bl2_bin_FILE} | awk -F " " '{print $5}'`
len_fip_bin_FILE=`ls -l ${fip_bin_FILE} | awk -F " " '{print $5}'`
len_env_bin_FILE=`ls -l ${env_bin_FILE} | awk -F " " '{print $5}'`
len_DTBS_FILE=`ls -l ${DTBS_FILE} | awk -F " " '{print $5}'`
len_IMAGE_FILE=`ls -l ${IMAGE_FILE} | awk -F " " '{print $5}'`
len_ROOTFS_FILE=`ls -l ${ROOTFS_FILE} | awk -F " " '{print $5}'`


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
	echo timer > /sys/class/leds/LED-Blue/trigger
	echo 50 > /sys/class/leds/LED-Blue/delay_off
	echo 50 > /sys/class/leds/LED-Blue/delay_on
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

# Erasing Spinand
erasing_spinand()
{
	#Before you write files to the mtd device, you need to perform an erase operation first 
	mtd_debug erase /dev/mtd0 0x0 0x300000
	mtd_debug erase /dev/mtd1 0x0 0xc0000
	mtd_debug erase /dev/mtd2 0x0 0x40000
	mtd_debug erase /dev/mtd3 0x0 0x1800000
	mtd_debug erase /dev/mtd4 0x0 0xe000000
}

burn_boot()
{
    #burn header_bin file
	mtd_debug write /dev/mtd0 0x0 ${len_header_bin_FILE} ${header_bin_FILE}
	cmd_check $? "Update header_bin file"

    #burn bl2_dtb file
	mtd_debug write /dev/mtd0 0xc0000 ${len_bl2_dtb_FILE} ${bl2_dtb_FILE}
	cmd_check $? "Update bl2_dtb file"

    #burn bl2_bin file
	mtd_debug write /dev/mtd0 0xe0000 ${len_bl2_bin_FILE} ${bl2_bin_FILE}
	cmd_check $? "Update bl2_bin file"

    #burn fip_bin file
	mtd_debug write /dev/mtd0 0x100000 ${len_fip_bin_FILE} ${fip_bin_FILE}
	cmd_check $? "Update fip_bin file"

	#burn env_bin file
	mtd_debug write /dev/mtd1 0x0 ${len_env_bin_FILE} ${env_bin_FILE}
	cmd_check $? "Update env_bin file"

    sleep 1
    sync
}

burn_image()
{
    #burn dtb file
    mtd_debug write /dev/mtd2 0x0 ${len_DTBS_FILE} ${DTBS_FILE}
	cmd_check $? "Update dtb file"

    #burn Image file
    mtd_debug write /dev/mtd3 0x0 ${len_IMAGE_FILE} ${IMAGE_FILE}
    cmd_check $? "Update Image file"

    sleep 1
	sync
}

burn_rootfs()
{
    #burn rootfs file
        mtd_debug write /dev/mtd4 0x0 ${len_ROOTFS_FILE} ${ROOTFS_FILE}
	cmd_check $? "Update rootfs"

	sync
}


updating
# pid=$!
clear


echo "---------------------------start erasing_spinand---------------------------" > /dev/ttyS0
draw_progress_bar 0 > /dev/ttyS0
printf "\r"  > /dev/ttyS0
erasing_spinand  > /home/root/log.txt &
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
    sleep 0.1
    printf "\r"  > /dev/ttyS0
done
draw_progress_bar 100 > /dev/ttyS0
echo " " > /dev/ttyS0
echo "---------------------------end burn_rootfs---------------------------" > /dev/ttyS0
clear
echo " " > /dev/ttyS0
#rootfs_result="{\"step\":\"firmware\",\"PN\":\"xxx\",\"SN\":\"xxx\",\"CN\":\"xxx\",\"result\":{\"module\":\"rootfs\",\"progress\":\"20\", \"state\":\"0\",\"message\":\"write rootfs succuess\"}}"
#echo ">>>[${#rootfs_result}]${rootfs_result}"

update_success
