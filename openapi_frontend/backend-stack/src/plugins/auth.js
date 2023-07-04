// 验证权限
import useUserStore from "@/store/modules/user"

function authPermission(permission) {
  const all_permission = ""
  const permissions = useUserStore().permissions
  if (permission && permission.length > 0) {
    return permission.some((e) => {
      return all_permission === v || v === permission
    })
  } else {
    return false
  }
}

function authRole(role) {
  const super_admin = "admin"
  const roles = useUserStore().roles
  if (role && role.length > 0) {
    return roles.some((v) => {
      return super_admin === v || v === role
    })
  } else {
    return false
  }
}

export default {
  // 验证用户是否具备某权限
  hasPermi(permission) {
    return authPermission(permission)
  },
  // 验证用户是否含有指定权限，只需包含其中一个即可
  hasPermiOr(permission) {
    return permission.some((item) => {
      return authPermission(item)
    })
  },
  // 验证用户是否含有指定权限，必须全部拥有
  hasPermiAnd(permission) {
    return permission.every((item) => {
      return authPermission(item)
    })
  },
  // 验证用户是否具备某角色
  hasRole(role) {
    return authRole(role)
  },
  // 验证用户是否含有指定角色，只需包含其中一个
  hasRoleOr(roles) {
    return roles.some((role) => {
      return authRole(role)
    })
  },
  // 验证用户是否含有指定角色，必须全部拥有
  hasRoleAnd(roles) {
    return roles.every((role) => {
      return authRole(role)
    })
  },
}
