package com.Maxim.File_storage_API;


import com.Maxim.File_storage_API.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.ZoneId;

//@EnableR2dbcRepositories
@SpringBootApplication
public class FileStorageApiApplication {


	public static void main(String[] args) {

		SpringApplication.run(FileStorageApiApplication.class, args);

	}
}
