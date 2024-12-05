const photos_uploadButton = document.getElementById("upload_button");

photos_uploadButton.addEventListener("click", function () {
    inputPhoto = document.getElementById("file_input");
    photo = inputPhoto.files[0];
    formData = new FormData();
    formData.append("file", photo);

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status == 302) {
                window.location = 'http://' + window.location.host + '/myapp-1.0-SNAPSHOT/login.html'
            } else if (xhr.status == 200) {
                alert(xhr.responseText);
                //refreshListThumbnails();

                //window.location = 'http://' + window.location.host + '/myapp-1.0-SNAPSHOT/'
            } else {
                alert(xhr.responseText);
            }
        }
    }

    xhr.open("PUT", "http://" + window.location.host + "/cloud24/photos");
    //xhr.setRequestHeader("Content-Type", "multipart/form-data");
    //xhr.setRequestHeader("Content-length", photo.length);
    xhr.send(formData);
}
)

const photos_listButton = document.getElementById("list_button");

photos_listButton.addEventListener("click", function () {

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 302) {
                //window.location = 'http://' + window.location.host + '/myapp-1.0-SNAPSHOT/login.html'
            } else if (xhr.status === 200) {
                var theaderRef = document.getElementById('thumbnails_table').getElementsByTagName('thead')[0];
                //
                while (theaderRef.firstChild) {
                    theaderRef.removeChild(theaderRef.firstChild);
                }

                // Insert a row at the end of the header
                var newHeaderRow = theaderRef.insertRow();
                var newHeaderKeyCell = document.createElement("TH");
                newHeaderKeyCell.innerHTML = "Key";
                newHeaderRow.appendChild(newHeaderKeyCell);
                var newHeaderSizeCell = document.createElement("TH");
                newHeaderSizeCell.innerHTML = "Size";
                newHeaderRow.appendChild(newHeaderSizeCell);

                var tbodyRef = document.getElementById('thumbnails_table').getElementsByTagName('tbody')[0];
                while (tbodyRef.firstChild) {
                    tbodyRef.removeChild(tbodyRef.firstChild);
                }
                var thumbnailsArray = JSON.parse(xhr.responseText);

                for (let i = 0; i < thumbnailsArray.length; i++) {
                    // Insert a row at the end of table
                    var newThumbnailRow = tbodyRef.insertRow();

                    var newThumbnailKeyCell = newThumbnailRow.insertCell();
                    var newThumbnailKey = document.createTextNode(thumbnailsArray[i].key);
                    newThumbnailKeyCell.appendChild(newThumbnailKey);

                    var newThumbnailSizeCell = newThumbnailRow.insertCell();
                    var newThumbnailSize = document.createTextNode(thumbnailsArray[i].size + "~" + "Kb");
                    newThumbnailSizeCell.appendChild(newThumbnailSize);

                    var newThumbnailDownloadCell = newThumbnailRow.insertCell();
                    var newThumbnailDownload = document.createElement("button");
                    newThumbnailDownload.innerHTML = "download";
                    addThumbnailClickHandler(newThumbnailDownload, thumbnailsArray[i].key);
                    newThumbnailDownloadCell.appendChild(newThumbnailDownload);
                }
            } else {
                alert(xhr.status + " : " + xhr.responseText);

            }
        }
    };

    xhr.open("GET", "https://3f0r5ulj18.execute-api.ap-southeast-1.amazonaws.com/TEST/photoslist");
    xhr.send(null);
}
)

function addThumbnailClickHandler(button, key) {
    button.addEventListener('click', function () {
        getBase64Image(key)
    });
}

function refreshListThumbnails() {
    var photos_listButton = document.getElementById("list_button");
    photos_listButton.click();
}


function getBase64Image(objName) {
    const xhr = new XMLHttpRequest();

    xhr.onload = function () {
        let reader = new FileReader();
        reader.onloadend = function () {
            if (xhr.status === 302) {
                alert("SECURITY ISSUE")
            } else if (xhr.status === 200) {
                var outputImg = document.getElementById('download_image');
                outputImg.src = reader.result;

            } else {
                alert(xhr.status + " : " + xhr.responseText);
            }
        };
        reader.readAsDataURL(xhr.response);
    };


    xhr.open("POST", "https://3f0r5ulj18.execute-api.ap-southeast-1.amazonaws.com/TEST/photo");
    const body = JSON.stringify({
        "objName": objName
    });
    xhr.responseType = 'blob';
    xhr.send(body);

}


