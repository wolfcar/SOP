package common

import (
	"bytes"
	"encoding/json"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"net/url"
	"os"
	"path/filepath"
	"strings"
)

type UploadFile struct {
	// 表单名称
	Name string
	Filepath string
}

// 请求客户端
var httpClient = &http.Client{}

func Get(reqUrl string, allParams map[string]string, headers map[string]string) string  {
	urlParams := url.Values{}
	Url, _ := url.Parse(reqUrl)
	for key, val := range allParams {
		urlParams.Set(key, val)
	}

	//如果参数中有中文参数,这个方法会进行URLEncode
	Url.RawQuery = urlParams.Encode()
	// 得到完整的url，http://xx?query
	urlPath := Url.String()

	httpRequest,_ := http.NewRequest("GET", urlPath, nil)
	// 添加请求头
	if headers != nil {
		for k, v := range headers {
			httpRequest.Header.Add(k,v)
		}
	}
	// 发送请求
	resp, err := httpClient.Do(httpRequest)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()
	response, _ := ioutil.ReadAll(resp.Body)
	return string(response)
}

func PostForm(reqUrl string, allParams map[string]string, headers map[string]string) string {
	return post(reqUrl, allParams, "application/x-www-form-urlencoded", nil, headers)
}

func PostJson(reqUrl string, allParams map[string]string, headers map[string]string) string {
	return post(reqUrl, allParams, "application/json", nil, headers)
}

func PostFile(reqUrl string, allParams map[string]string, files []UploadFile, headers map[string]string) string {
	return post(reqUrl, allParams, "multipart/form-data", files, headers)
}


func post(reqUrl string, allParams map[string]string, contentType string, files []UploadFile, headers map[string]string) string  {
	requestBody, realContentType := getReader(allParams, contentType, files)
	httpRequest,_ := http.NewRequest("POST", reqUrl, requestBody)
	// 添加请求头
	httpRequest.Header.Add("Content-Type", realContentType)
	if headers != nil {
		for k, v := range headers {
			httpRequest.Header.Add(k,v)
		}
	}
	// 发送请求
	resp, err := httpClient.Do(httpRequest)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()
	response, _ := ioutil.ReadAll(resp.Body)
	return string(response)
}

func getReader(allParams map[string]string, contentType string, files []UploadFile) (io.Reader, string)  {
	if strings.Index(contentType, "json") > -1 {
		bytesData, _ := json.Marshal(allParams)
		return bytes.NewReader(bytesData), contentType
	} else if files != nil {
		body := &bytes.Buffer{}
		// 文件写入 body
		writer := multipart.NewWriter(body)
		for _, uploadFile := range files {
			file, err := os.Open(uploadFile.Filepath)
			if err != nil {
				panic(err)
			}
			part, err := writer.CreateFormFile(uploadFile.Name, filepath.Base(uploadFile.Filepath))
			if err != nil {
				panic(err)
			}
			_, err = io.Copy(part, file)
			file.Close()
		}
		// 其他参数列表写入 body
		for k, v := range allParams {
			if err := writer.WriteField(k, v); err != nil {
				panic(err)
			}
		}
		if err := writer.Close(); err != nil {
			panic(err)
		}
		// 上传文件需要自己专用的contentType
		return body, writer.FormDataContentType()
	} else {
		urlValues := url.Values{}
		for key, val := range allParams {
			urlValues.Set(key, val)
		}
		reqBody:= urlValues.Encode()
		return strings.NewReader(reqBody), contentType
	}
}
