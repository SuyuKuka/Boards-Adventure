package model.config.role;

public final class RoleFactory {
    public static Role createRole(final String role) {
        switch (role) {
            case "FARMER":
                return new Farmer();
            default:
                return new Nothing();
        }
    }
}
