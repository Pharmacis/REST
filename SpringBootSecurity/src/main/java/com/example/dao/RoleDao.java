package com.example.dao;

import com.example.model.Role;

import java.util.List;

public interface RoleDao {
    void addRole(Role role);
    Role getRoleByName(String name);
    Role update(Role role);
    Long countRoles(String name);
    List<String> getRoles();
    List <Role> listRolesByUser(Long userId);



}