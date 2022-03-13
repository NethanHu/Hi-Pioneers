camera.js
/**
 * 简单类
 * 关于检测相机，打开相机
 * 2020 0320
 *es6
 */
const Camera = function () {
    var videoDeviceElement = null;
    var videoElement = null;
    var videoSetting = {width: 320, height: 240};
    var canvasElement = null;
    var canvasContext = null;
    /**
     * 列出所有摄像头
     * @param videoDevice
     * @returns {Promise}
     */
    this.listCamera = function(videoDevice) {
        videoDeviceElement = videoDevice;
        return new Promise((resolve, reject) => {
            navigator.mediaDevices.enumerateDevices()
                .then((devices) => {
                    devices.find((device) => {
                        if (device.kind === 'videoinput') {
                            const option = document.createElement('option');
                            option.text = device.label || 'camera '+ (videoDeviceElement.length + 1).toString();
                            option.value = device.deviceId;
                            // 将摄像头id存储在select元素中，方便切换前、后置摄像头
                            videoDeviceElement.appendChild(option);
                        }
                    });
                    if (videoDeviceElement.length === 0) {
                        reject('没有摄像头');
                    } else {
                        videoDeviceElement.style.display = 'inline-block';
                        // 创建canvas，截取摄像头图片时使用
                        // canvasElement = document.createElement('canvas');
                        // canvasContext = canvasElement.getContext('2d');
                        resolve(true);

                    }

                })
                .catch((err) => {
                    reject(err);
                });
        });
    };
    /**
     * 打开摄像头
     * @param video
     * @param deviceId
     * @param setting
     * @returns {Promise}
     */
    this.openCamera = function(video, deviceId, setting) {
        videoElement = video;
        if (setting) {
            videoSetting = setting;
        }
        // 摄像头参数
        // 更多参数请查看 https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints
        const constraints = {
            audio: false,
            video: {deviceId: {exact: deviceId}}

        };
        // 如果是切换摄像头，则需要先关闭。
        if (videoElement.srcObject) {
            videoElement.srcObject.getTracks().forEach((track) => {
                track.stop();
            });
        }
        return new Promise((resolve, reject) => {
            navigator.mediaDevices.getUserMedia(constraints)
                .then((stream) => {
                    videoElement.srcObject = stream;
                    videoElement.style.display = 'block';
                    videoElement.onloadedmetadata = function(){
                        resolve(true);
                    };
                    videoElement.play();
                })
                .catch((err) => {
                    reject(err);
                });
        });
    };

};