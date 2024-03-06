package com.memories.api;

import com.memories.api.file.FileAttachment;
import com.memories.api.memory.Memory;
import com.memories.api.memory.MemoryRepository;
import com.memories.api.user.User;
import com.memories.api.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


@SpringBootApplication
public class MemoriesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoriesApiApplication.class, args);
	}
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner userCreator(UserRepository userRepository,
								  PasswordEncoder passwordEncoder,
								  MemoryRepository memoryRepository){
		return (args) -> {
			int i =  1;
			for (; i <= 25;i++){
				User user = new User();
				user.setUsername("user"+i);
				user.setEmail("user"+i+"@mail.com");
				user.setPassword(passwordEncoder.encode("1234"));
				user.setActive(true);
				userRepository.save(user);
				for (int j=0 ; j<25; j++) {
					Memory memory = new Memory();
					memory.setContent("memory : " + j);
					memory.setTimestamp(new Date());
					memory.setUser(user);
					memoryRepository.save(memory);
				}
			}

		};
	}


}
