package com.gustavosdaniel.ecommerce_api.user;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> findUserByName(String userName){
        return (root, query, criteriaBuilder) -> {

            if(userName == null || userName.trim().isEmpty()){

                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("userName")),
                    "%" + userName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> findUserByUserRole(UserRole role){

        return (root, query, criteriaBuilder) -> {

            if(role == null){

                return null;
            }

            return criteriaBuilder.equal(root.get("userRole"), role);
        };
    }

    public static Specification<User> findUserByCpf(String cpf){

        return (root, query, criteriaBuilder) -> {

            if(cpf == null || cpf.trim().isEmpty()){

                return null;
            }

            return criteriaBuilder.equal(root.get("cpf"), cpf);
        };
    }

    public static Specification<User> findUserByPhoneNumber(String phoneNumber){

        return (root, query, criteriaBuilder) -> {

            if(phoneNumber == null || phoneNumber.trim().isEmpty()){

                return null;
            }

            return criteriaBuilder.like(root.get("phoneNumber"),
            "%" + phoneNumber + "%");
        };
    }

    public static Specification<User> filter(
            String userName, UserRole userRole, String cpf, String phoneNumber
    ){
        return Specification.allOf(
                findUserByName(userName),
                findUserByUserRole(userRole),
                findUserByCpf(cpf),
                findUserByPhoneNumber(phoneNumber)
        );
    }
}
