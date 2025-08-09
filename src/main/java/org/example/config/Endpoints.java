package org.example.config;

public class Endpoints {
    public static class User {
        public static final String CREATE = "/api/auth/register";
        public static final String LOGIN = "/api/auth/login";
        public static final String DELETE = "/api/auth/user";
    }

    public static class Order {
        public static final String CREATE = "/api/orders";
    }

    public static class Ingredient {
        public static final String GET_ALL = "/api/ingredients";
    }
}
