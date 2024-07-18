DESCRIPTION = "ma35d1 U-Boot suppporting ma35d1 ev boards."
#SECTION = "bootloaders"

DEPENDS += "dtc-native bc-native flex-native bison-native u-boot-scr-ma35d1"

unset _PYTHON_SYSCONFIGDATA_NAME

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

#UBOOT_SRC ?= "git://github.com/OpenNuvoton/MA35D1_u-boot-v2020.07.git;branch=master;protocol=https"
UBOOT_SRC ?= "git:///media/home/beste/01_Pro/ma35/bsp/new/myir-nuvoton-uboot;branch=feature-v2020.07;protocol=file"

#SRCBRANCH = "2020.07"
SRCBRANCH = "feature-v2020.07"
SRC_URI = "${UBOOT_SRC}"
#UBOOT_SRCREV = "feature-v2020.07"
UBOOT_SRCREV = "${AUTOREV}"
SRCREV = "${UBOOT_SRCREV}"

SRC_URI += " file://uEnv-spinand-ubi.cfg \
             file://uEnv-nand-ubi.cfg \
           "
PV = "${SRCBRANCH}"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION ?= "-${SRCBRANCH}"

BOOT_TOOLS = "u-boot-tools"
