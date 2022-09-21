def activeProfiles = project.activeProfiles
project.properties.springProfilesActive = activeProfiles.collect{ it.id }
	.join(',')

def message = (activeProfiles.size() == 1) ? '1 profile is active: '
	: "${activeProfiles.size()} profiles are active: "
log.info """The following ${message}${activeProfiles.collect{ "\"${it.id}\"" }
	.join(', ')}"""
