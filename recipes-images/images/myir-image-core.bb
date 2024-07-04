SUMMARY = "MYiR Image Core for MYD-LMA35."
LICENSE = "Proprietary"

inherit core-image 

IMAGE_LINGUAS = "en-us zh-cn"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

PACKAGECONFIG_append = " examples accessibility "

IMAGE_INSTALL_append = "\
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    ttf-dejavu-sans-condensed \
    ttf-dejavu-serif \
    ttf-dejavu-serif-condensed \
    ttf-dejavu-common \
    directfb-env \
    v4l-utils \
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
    libdrm \
    fbset \
    psplash \
    v4l-utils \
    alsa-utils \
    bc \
    pv \
    dbus \
    wpa-supplicant \
    ntp \
    libmodbus \
    myir-utils \
    start-service \
    ppp-quectel \
    hostapd \
    udev-extraconf \
    libubootenv \
    libmnl \
    expand-part \
    u-boot-nuvoton-fw-utils \
    igh-ethercat \
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

