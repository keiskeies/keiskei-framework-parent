spring:
  profiles:
    active: <#noparse>${ACTIVE_PROFILE:@environment@}</#noparse>
  application:
    name: ${name}




