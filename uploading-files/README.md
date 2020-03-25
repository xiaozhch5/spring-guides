# Uploading files
本节指引你如何使用springboot创建一个应用用于文件上传。

## 指引
首先创建FileUploadController.java

com/weilian/uf/controller/FileUploadController.java

````java
package com.weilian.uf.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import com.weilian.uf.service.StorageFileNotFoundException;
import com.weilian.uf.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class UploadFileController {

    private StorageService storageService;

    @Autowired
    public UploadFileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UploadFileController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
````
`FileUploadController.java`使用`@Controller`注释，所以Spring MVC可以找到它并路由。

`FileUploadController.java`类中的方法用`@GetMapping`或者`@PostMapping`注释来将`path`以及HTTP请求和特定的controller action绑定。

在本例子中，
* `GET /` : 从StorageService中寻找已经上传的文件，然后将其导入Thymeleaf template。与此同时，通过使用`MvcUriComponentsBuilder`计算出资源对应的链接。
* `GET /files/{filename}` : 使用`ContentDisposition` response header加载资源（如果存在的话）然后将其发送到浏览器让其下载。
* `POST /` : 处理一个file的multi-part message，然后将其发送到StorageService保存。

Note：在生产环境中，你最好将文件保存在一个特定的位置，数据库，或者一个非结构化数据库。最好不要以应用上下文为基础，也就是放在内存中。

在上面所说的例子中，`StorageService.java`如下:
````java
package com.weilian.uf.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
````
创建HTML Template，`uploadForm.html`文件展示了如何上传文件以及上传什么文件 
````html
<html xmlns:th="https://www.thymeleaf.org">
<body>

	<div th:if="${message}">
		<h2 th:text="${message}"/>
	</div>

	<div>
		<form method="POST" enctype="multipart/form-data" action="/">
			<table>
				<tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
				<tr><td></td><td><input type="submit" value="Upload" /></td></tr>
			</table>
		</form>
	</div>

	<div>
		<ul>
			<li th:each="file : ${files}">
				<a th:href="${file}" th:text="${file}" />
			</li>
		</ul>
	</div>

</body>
</html>
````
在application.properties文件中配置文件上传大小：
````properties
spring.servlet.multipart.max-file-size=128000KB
spring.servlet.multipart.max-request-size=128000KB
````
## 运行
直接运行`UfApplication.java`运行项目。

在浏览器中打开`localhost:8080`可以看到：
![image](https://github.com/xiaozhch5/spring-guides/uploading-files/images/uplpad.png)
