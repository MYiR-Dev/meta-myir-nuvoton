SUMMARY = "MYiR Image Core for MYD-LMA35."
LICENSE = "Proprietary"

inherit core-image 

IMAGE_LINGUAS = "en-us zh-cn"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

PACKAGECONFIG_append = " examples accessibility "

IMAGE_INSTALL_append = "\
    mtd-utils \
    ncurses \
    can-utils \
    coreutils \
    cpufrequtils \
    dosfstools \
    ethtool \
    evtest \
    e2fsprogs-mke2fs \
    e2fsprogs-resize2fs \
    fbset \
    iproute2 \
    iw \
    i2c-tools \
    libgpiod-tools \
    linuxptp \
    memtester \
    minicom \
    mmc-utils \
    nano \
    parted \
    ptpd \
    ntp \
    fac-burn-flash \
    "

# Define to null ROOTFS_MAXSIZE to avoid partition size restriction
IMAGE_ROOTFS_MAXSIZE = ""

#
# Multimedia part addons
#
IMAGE_MM_PART = " \
    tiff \
    libv4l \
    rc-keymaps \
    "

#
# Display part addons
#
IMAGE_DISPLAY_PART = " \
    fb-test         \
    "

CORE_IMAGE_EXTRA_INSTALL += " \
    \
    \
    ${IMAGE_DISPLAY_PART}       \
    ${IMAGE_MM_PART}            \
    \
    "

