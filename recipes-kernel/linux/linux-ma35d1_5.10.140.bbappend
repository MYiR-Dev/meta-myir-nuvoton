FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'rtlinux', \
    'file://0001-FEAT-Modify-default-log-level-is-1.patch \
     file://0002-FEAT-Support-nuvoTon-rt-linux-patch.patch \
     file://0003-FEAT-Fixed-the-SPI-compilation-error-caused-by-the-R.patch \
     file://0004-FEAT-Add-preempt-rt-in-defconfig.patch ', '', d)}"

KERNEL_LOCALVERSION = "${@bb.utils.contains('MACHINE_FEATURES', 'rtlinux', '-rt', '', d)}"
