// package com.ManShirtShop;

// import com.ManShirtShop.entities.Role;
// import com.ManShirtShop.repository.RoleRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.stereotype.Component;

// import java.sql.Timestamp;
// import java.util.Arrays;
// import java.util.List;

// @Component
// public class RoleInitializer implements ApplicationRunner {

//     private final RoleRepository roleRepository;

//     @Autowired
//     public RoleInitializer(RoleRepository roleRepository) {
//         this.roleRepository = roleRepository;
//     }

//     @Override
//     public void run(ApplicationArguments args) {
//         List<RoleEnum> roles = Arrays.asList(
//                 new RoleEnum("Admin", 1),
//                 new RoleEnum("Manager", 2),
//                 new RoleEnum("Customer", 3)
//         );

//         for (RoleEnum roleEnum : roles) {
//             Role existingRole = roleRepository.findByStatus(roleEnum.getStatus());
//             if (existingRole == null) {
//                 Role role = new Role();
//                 role.setName(roleEnum.getName());
//                 role.setStatus(roleEnum.getStatus());
//                 roleRepository.save(role);
//             }
//         }
//     }

//     private static class RoleEnum {
//         private String name;
//         private int status;

//         public RoleEnum(String name, int status) {
//             this.name = name;
//             this.status = status;
//         }

//         public String getName() {
//             return name;
//         }

//         public int getStatus() {
//             return status;
//         }
//     }
// }
