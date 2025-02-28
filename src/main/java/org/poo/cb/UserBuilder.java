package org.poo.cb;

// Am ales un builder pentru ca imbunatateste modularitatea codului si pentru flexibilitatea oferita
public class UserBuilder {
    private User user = new User();

    public User build() {
        return user;
    }

    public UserBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withFirstname(String firstname) {
        user.setFirstname(firstname);
        return this;
    }

    public UserBuilder withLastname(String lastname) {
        user.setLastname(lastname);
        return this;
    }

    public UserBuilder withAddress(String address) {
        user.setAddress(address);
        return this;
    }
}
