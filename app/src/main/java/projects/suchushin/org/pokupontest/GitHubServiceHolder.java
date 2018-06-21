package projects.suchushin.org.pokupontest;

class GitHubServiceHolder{
    private static GitHubService service;

    public static GitHubService getService() {
        return service;
    }

    public static void setService(GitHubService service) {
        GitHubServiceHolder.service = service;
    }
}
