SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_LINGUAS = "en-us zh-cn"

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "" ,d)}"

IMAGE_FEATURES += " ssh-server-dropbear"

IMAGE_INSTALL_append = "\
    kernel-modules \
    directfb-env \
    v4l-utils \
    tslib-tests \
    tslib-calibrate \
    tzdata \
    memtester \
    i2c-tools \
    mmc-utils \
    mtd-utils \
    busybox \
    evtest \
    serialcheck \
    avahi-daemon \
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
    openssl \
    readline \
    grep \
    sed \
    libdrm \
    fbset \
    psplash \
    v4l-utils \
    alsa-utils \
    wpa-supplicant \
    myir-utils \
    start-service \
    ppp-quectel \
    hostapd \
    expand-part \
    u-boot-nuvoton-fw-utils \
    igh-ethercat \
    temp-ctrl \
    libubootenv \
    bc \
    pv \
    libsocketcan \
    canutils \
    coreutils \
    util-linux \
"

ROOTFS_POSTPROCESS_COMMAND += "remove_busybox_dd; "

remove_busybox_dd () {
    rm -f ${IMAGE_ROOTFS}/bin/dd
    cp ${IMAGE_ROOTFS}/bin/dd.coreutils ${IMAGE_ROOTFS}/bin/dd
}
