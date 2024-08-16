var LocalizeService = {
    sendingRequest: false,
    changeLanguage: function (e) {
        console.log(e.currentTarget);

        if (LocalizeService.sendingRequest) {
            return false;
        }
        LocalizeService.sendingRequest = true;

        let formData = new FormData();
        formData.append("language", e.currentTarget.value);

        let request = new XMLHttpRequest();
        request.open("POST", "/localize", true);
        request.send(formData);
        request.onload = function () {
            if (request.status == 200) {
                try {
                    answer = JSON.parse(request.responseText);
                    if ("code" in answer || answer.code == 2) {
                        location.reload(true);
                    }
                } catch (e) {
                }
            }
            LocalizeService.sendingRequest = false;
        };
    }
};