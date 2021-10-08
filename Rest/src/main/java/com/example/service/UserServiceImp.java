package com.example.service;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.model.Role;
import com.example.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

@Service()
@Transactional
public class UserServiceImp implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        Set<Role> roles = new HashSet<> ();
        for ( Role role: user.getRoles()) {
            roles.add (roleRepository.getRoleByName (role.getName ()));
        }
        user.setRoles (roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional
    public User updateUser(User user){
        User userByDB = userRepository.getOne (user.getId ());
        if(bCryptPasswordEncoder.matches (user.getPassword (),userByDB.getPassword ())
        || user.getPassword ()=="San"){
        }else{
            user.setPassword (bCryptPasswordEncoder.encode (user.getPassword ()));
        }
        if(user.getRoles ()!=null) {
            Set<Role> roles = new HashSet<> ();
            for (Role role : user.getRoles ()) {
                roles.add (roleRepository.getRoleByName (role.getName ()));
            }
            user.setRoles (roles);
        }else{
            user.setRoles (userByDB.getRoles ());
        }
        return userRepository.save (user);
    }
}
