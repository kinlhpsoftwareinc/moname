def activeProfiles = project.activeProfiles

project.properties.springProfilesActive = activeProfiles
		.collect{ it.id.replaceAll(/.*keycloak.*/, 'keycloak') }.unique().join(',')

def countedProfilesMessage = (activeProfiles.size() == 1)
		? '1 profile is active: '
		: "${activeProfiles.size()} profiles are active: "

log.info """The following ${countedProfilesMessage}${activeProfiles
		.collect{ "\"${it.id}\"" }.join(', ')} (resolved to spring.profiles.active="${project.properties.springProfilesActive}")"""
