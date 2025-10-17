def activeProfiles = project.activeProfiles

project.properties.springProfilesActive = activeProfiles
		.collect{ it.id.replaceAll(/.*keycloak.*/, 'keycloak') }
		.unique()
		.join(',')

/*
project.properties.springProfilesActive = activeProfiles
		.collectMany{ it.id.contains('keycloak') ? ['keycloak', it.id] : [it.id] }
		.unique()
		.join(',')
 */

def countedProfilesMessage = (activeProfiles.size() == 1)
		? '1 profile is active: '
		: "${activeProfiles.size()} profiles are active: "

log.warn 'At this point, there is no way to know how the profile groups (spring.profiles.group) will be expanded'
log.info """The following ${countedProfilesMessage}${activeProfiles
		.collect{ "\"${it.id}\"" }.join(', ')} (resolved to spring.profiles.active="${project.properties.springProfilesActive}")"""
