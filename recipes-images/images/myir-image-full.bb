SUMMARY = "NUC3500 example of image based on QT framework."
LICENSE = "Proprietary"

inherit populate_sdk_qt5

inherit core-image 

IMAGE_LINGUAS = "en-us"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

#IMAGE_FSTYPES_remove = " ubi spinand nand"

PACKAGECONFIG_append = " examples accessibility "

IMAGE_INSTALL_append = "\
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    ttf-dejavu-sans-condensed \
    ttf-dejavu-serif \
    ttf-dejavu-serif-condensed \
    ttf-dejavu-common \
    qt5-env \
    directfb-env \
    v4l-utils \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    tslib-tests \
    tslib-calibrate \
    tzdata \
    memtester \
    i2c-tools \
    mmc-utils \
    mtd-utils \
    can-utils \
    busybox \
    util-linux \
    gdb \
    evtest \
    serialcheck \
    perf \
    python3 \
    avahi-daemon \
    perl \
    sqlite3 \
    ethtool \
    net-tools \
    iptables \
    iperf3 \
    iproute2 \
    proftpd \
    tftp-hpa \
    ppp \
    quectel-cm \
    tcpdump \
    bridge-utils \
    bluez5 \
    openssl \
    ncurses \
    readline \
    grep \
    sed \
    gawk \
    qtbase  \
    qtsvg \
    qtquickcontrols \
    qtquickcontrols2 \
    qtmultimedia \
    qtvirtualkeyboard \
    qtgraphicaleffects \
    libdrm \
    fbset \
    psplash \
    v4l-utils \
    alsa-utils \
    bc \
    pv \
    dbus \
    hmi \
    wpa-supplicant \
    ntp \
    "

# Define to null ROOTFS_MAXSIZE to avoid partition size restriction
IMAGE_ROOTFS_MAXSIZE = ""

#
# Multimedia part addons
#
IMAGE_MM_PART = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer', 'packagegroup-gstreamer1-0', '', d)} \
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

#
# QT part Essentials
#
IMAGE_QT_MANDATORY_PART = " \
   qtbase                  \
   liberation-fonts        \
   qtbase-plugins          \
   qtbase-tools            \
   \
   qtdeclarative           \
   qtdeclarative-qmlplugins\
   qtdeclarative-tools     \
   \
   qtgraphicaleffects      \
   qtgraphicaleffects-qmlplugins \
   \
   qtmultimedia            \
   qtmultimedia-plugins    \
   qtmultimedia-qmlplugins \
   \
   qtscript                \
   \
   nvt-qt5                 \
   "

#
# QT part add-ons
#
IMAGE_QT_OPTIONAL_PART = " \
   qtsvg                   \
   qtsvg-plugins           \
   \
   qtlocation              \
   qtlocation-qmlplugins   \
   qtlocation-plugins      \
   \
   \
   qtquickcontrols         \
   qtquickcontrols-qmlplugins \
   qtquickcontrols2         \
   qtquickcontrols2-qmlplugins\
   qtscript                \
   \
   qtsensors               \
   qtserialport            \
   \
   qtcharts                \
   qtcharts-qmlplugins     \
   \
   qtlocation              \
   qtlocation-plugins      \
   qtlocation-qmlplugins   \
   "

#
# QT part examples
#
IMAGE_QT_EXAMPLES_PART = " \
   qtbase-examples         \
   \
   qt5everywheredemo       \
   "

CORE_IMAGE_EXTRA_INSTALL += " \
    \
    \
    ${IMAGE_DISPLAY_PART}       \
    ${IMAGE_MM_PART}            \
    \
    ${IMAGE_QT_MANDATORY_PART}  \
    ${IMAGE_QT_OPTIONAL_PART}   \
    ${IMAGE_QT_EXAMPLES_PART}   \
    "

