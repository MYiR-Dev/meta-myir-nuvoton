SUMMARY = "for sdcard program"
DESCRIPTION = "use sdcard boot up and program full image to emmc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit  systemd

S = "${WORKDIR}"

SRC_URI = "file://home/root/burn_emmc.sh \
	   file://home/root/burn_spinand.sh \
	    file://home/root/Manifest-spinand \
	    file://home/root/Manifest-emmc \
	   file://fac-burn-flash.service \
           file://licenses/GPL-2 \
          "

do_install(){
  install -d ${D}${systemd_system_unitdir}
	install -d ${D}/home/root/
	install -d ${D}/home/root/lma35_images

	install -m 755 ${WORKDIR}/fac-burn-flash.service ${D}${systemd_system_unitdir}/fac-burn-flash.service

	if [ MACHINENAME == "myir-image-emmc" ];then
        	install -m 755 ${WORKDIR}/home/root/burn_emmc.sh ${D}/home/root/burn_flash.sh
        	install -m 755 ${WORKDIR}/home/root/Manifest-emmc ${D}/home/root/lma35_images/Manifest
		install -m 755 ${DEPLOY_DIR_IMAGE}/myir-image-full-${MACHINENAME}.ext4  ${D}/home/root/lma35_images/rootfs-full.ext4
	elif [ MACHINENAME == "myir-image-spinand" ];then
		install -m 755 ${WORKDIR}/home/root/burn_spinand.sh ${D}/home/root/burn_flash.sh
        	install -m 755 ${WORKDIR}/home/root/Manifest-spinand ${D}/home/root/lma35_images/Manifest
		install -m 755 ${DEPLOY_DIR_IMAGE}/myir-image-core-${MACHINENAME}.ubi ${D}/home/root/lma35_images/rootfs-core.ubi
	fi
	
	for i in ${IMAGE_BOOT_FILES};do
		install -m 755 ${DEPLOY_DIR_IMAGE}/${i} ${D}/home/root/lma35_images/${i}
	done
	
}


FILES_${PN} = "/"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "fac-burn-flash.service"
SYSTEMD_AUTO_ENABLE = "enable"
