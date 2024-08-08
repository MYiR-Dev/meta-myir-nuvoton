FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'rtlinux', \
    'file://0001-FEAT-Add-isolcpus-1-in-bootargs.patch', '', d)}"
