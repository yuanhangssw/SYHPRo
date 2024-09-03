const getters = {
  token: (state) => state.user.token,
  avatar: (state) => state.user.avatar,
  name: (state) => state.user.name,
  roles: (state) => state.user.roles,
  permissions: (state) => state.user.permissions,
  projectList: (state) => state.user.projectList,
  projectId: (state) => state.user.projectId,
  userId: (state) => state.user.userId,
  phone: (state) => state.user.phone,
  isAddNewPatrol: (state) => state.user.isAddNewPatrol,
};
export default getters;
