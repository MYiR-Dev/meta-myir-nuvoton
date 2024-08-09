SUMMARY = "myir utils 2.0"
DESCRIPTION = "myir utils application"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}"

SRC_URI = "file://licenses/GPL-2 \
                   file://uart_test  \
                   file://uart_test_485  \
                   file://uart_test_232  \
                   file://watchdog_test  \
                   file://etc/myir_test/myir_audio_play  \
                   file://etc/myir_test/myir_camera_play  \
                   file://etc/myir_test/myir_dial  \
                   file://etc/myir_test/wifi_on_ap  \
                   file://etc/myir_test/wifi_on_sta  \
                   file://etc/myir_test/myir_video_play  \
		   file://20-static-usb0.network \
		   file://10-statuc-eth0.network \
		   file://10-statuc-eth1.network \
		   file://sw-version \
		   file://rtl8822cu_config \
		   file://rtl8822cu_fw \
		   file://eeprom_test \
		   file://framebuffer_test \
		   file://myir_udhcpd.conf \
		   file://myir_hostapd.conf \
		   file://v4l2grab \
          "
          
inherit  systemd

S = "${WORKDIR}"
do_install (){
	install -d ${D}${systemd_system_unitdir}
	install -d ${D}${systemd_system_unitdir}/../network
	install -d ${D}${datadir}
	install -d ${D}/etc
	install -d ${D}/etc/myir_test
	install -d ${D}/usr/bin
	install -d ${D}/lib/firmware

        install -m 755 ${WORKDIR}/etc/myir_test/myir_audio_play ${D}/etc/myir_test
        install -m 755 ${WORKDIR}/etc/myir_test/myir_camera_play ${D}/etc/myir_test
        install -m 755 ${WORKDIR}/etc/myir_test/myir_video_play ${D}/etc/myir_test
        install -m 755 ${WORKDIR}/etc/myir_test/myir_dial ${D}/etc/myir_test
        install -m 755 ${WORKDIR}/etc/myir_test/wifi_on_ap ${D}/etc/myir_test
        install -m 755 ${WORKDIR}/etc/myir_test/wifi_on_sta ${D}/etc/myir_test	
        install -m 755 ${WORKDIR}/uart_test ${D}/usr/bin/uart_test
        install -m 755 ${WORKDIR}/uart_test_485 ${D}/usr/bin/uart_test_485
        install -m 755 ${WORKDIR}/uart_test_232 ${D}/usr/bin/uart_test_232
        install -m 755 ${WORKDIR}/watchdog_test ${D}/usr/bin/watchdog_test
        install -m 755 ${WORKDIR}/eeprom_test ${D}/usr/bin/eeprom_test
        install -m 755 ${WORKDIR}/v4l2grab ${D}/usr/bin/v4l2grab
        install -m 755 ${WORKDIR}/framebuffer_test ${D}/usr/bin/framebuffer_test
        install -m 755 ${WORKDIR}/myir_hostapd.conf ${D}/etc/myir_hostapd.conf
        install -m 755 ${WORKDIR}/myir_udhcpd.conf ${D}/etc/myir_udhcpd.conf

        install -m 755 ${WORKDIR}/rtl8822cu_fw ${D}/lib/firmware/rtl8822cu_fw
        install -m 755 ${WORKDIR}/rtl8822cu_config ${D}/lib/firmware/rtl8822cu_config

	install -m 644 ${WORKDIR}/20-static-usb0.network ${D}${systemd_system_unitdir}/../network/20-static-usb0.network
	install -m 644 ${WORKDIR}/10-statuc-eth0.network ${D}${systemd_system_unitdir}/../network/10-statuc-eth0.network
	install -m 644 ${WORKDIR}/10-statuc-eth1.network ${D}${systemd_system_unitdir}/../network/10-statuc-eth1.network
	install -m 644 ${WORKDIR}/sw-version ${D}/etc/sw-version
	
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_AUTO_ENABLE = "enable"

FILES_${PN} = "/"
INSANE_SKIP_${PN} = "file-rdeps"
