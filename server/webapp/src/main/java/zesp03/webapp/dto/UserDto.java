/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto;

import zesp03.common.entity.User;
import zesp03.common.entity.UserRole;

public class UserDto {
    private long id;
    private String name;
    private boolean activated;
    private boolean blocked;
    private UserRole role;
    private long createdAt;
    private long updatedAt;
    private long lastAccess;// timestamp w milisekundach

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isRoot() {
        return role == UserRole.ROOT;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void wrap(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.activated = u.isActivated();
        this.blocked = u.isBlocked();
        this.role = u.getRole();
        this.lastAccess = u.getLastAccess().getTime();
        this.createdAt = u.getCreatedAt().getTime();
        this.updatedAt = u.getUpdatedAt().getTime();
    }

    public static UserDto make(User u) {
        UserDto dto = new UserDto();
        dto.wrap(u);
        return dto;
    }
}
