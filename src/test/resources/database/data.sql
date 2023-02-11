INSERT INTO `client` (`id`, `name`, `created`, `updated`, `status`)
VALUES
	(1, 'dalisaloom', '2018-11-02 20:26:09', '2018-11-02 20:26:57', 'A');

INSERT INTO `client_branch` (`id`, `client_id`, `name`, `latitude`, `longitude`, `created`, `updated`, `status`)
VALUES
	(1, 1, 'co-monteria-dalisaloom-01', 8.76219000, -75.86524800, '2018-11-02 20:28:27', '2018-11-02 20:28:31', 'A');

INSERT INTO `user` (`id`, `client_id`, `fname`, `lname`, `email`, `phone`, `birthdate`, `gender`, `created`, `updated`, `status`)
VALUES
	(1, 1, 'Andrés', 'Jiménez', 'andres@hatit.co', 3017549852, NULL, 'M', '2018-11-02 20:31:30', '2018-11-02 20:31:30', ''),
	(2, 1, 'Jairo', 'Villadiego', 'jairo@hatit.co', NULL, NULL, 'M', '2018-11-02 20:31:30', '2018-11-02 20:31:30', ''),
	(3, 1, 'Samir', 'Llorente', 'samir@hatit.co', 3022800178, NULL, 'M', '2018-11-02 20:31:30', '2018-11-02 20:31:30', '');

INSERT INTO `device` (`mac`, `os`, `lang`, `touch_points`, `platform`, `user_agent`, `width`, `height`, `created`, `updated`, `status`)
VALUES
	('74:df:bf:10:ef:ec', 'Intel Mac OS X 10.12', 'en-US', 0, NULL, NULL, 1440, 900, '2018-11-02 20:43:32', '2018-11-02 20:44:23', 'A'),
	('74:df:bf:10:af:ec', 'Intel Mac OS X 10.12', 'en-US', 0, NULL, NULL, 1440, 900, '2018-11-02 20:43:32', '2018-11-02 20:44:23', 'A');

INSERT INTO `device_user` (`mac`, `user_id`, `client_id`, `created`, `updated`, `status`)
VALUES
	('74:df:bf:10:ef:ec', 1, 1, '2018-11-03 05:50:59', '2018-11-03 05:51:02', 'A');

INSERT INTO `access_point` (`id`, `client_id`, `client_branch_id`, `mac`, `created`, `updated`, `status`)
VALUES
	(1, 1, 1, '60:3D:26:BC:8B:D0', '2018-11-02 20:46:57', '2018-11-02 20:46:57', 'A');

INSERT INTO `setting` (`id`, `client_id`, `client_branch_id`, `name`, `value`, `created`, `updated`, `status`)
VALUES
	(1, 1, NULL, 'a-client-setting', 'a-value', '2018-11-16 17:31:54', '2018-11-16 17:31:54', 'A'),
	(2, 1, 1, 'a-branch-setting', 'a-value', '2018-11-16 17:31:54', '2018-11-16 17:31:54', 'A'),
	(3, 1, NULL, 'an-overridden-setting', 'a-value', '2018-11-16 17:31:54', '2018-11-16 17:31:54', 'A'),
	(4, 1, 1, 'an-overridden-setting', 'a-value', '2018-11-16 17:31:54', '2018-11-16 17:31:54', 'A');


-- INSERT INTO `session` (`id`, `device_mac`, `user_id`, `ip`, `logout_datetime`, `network_in`, `network_out`, `created`, `updated`, `status`)
-- VALUES
	-- (1, '74:df:bf:10:ef:ec', 1, '192.168.0.1', NULL, NULL, NULL, '2018-07-17 16:39:01', '2018-07-17 16:39:01', 'A');
