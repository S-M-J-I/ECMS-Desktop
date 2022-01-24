package project.models;

public class ClubForum {

    private int forumID;
    private String name;
    private String username;
    private String password;
    private String settingsPass;
    private boolean isRecruitmentOn;
    private String hrPass;
    private String prPass;
    private String rndPass;
    private String treasuryPass;
    private String primary_color;
    private String secondary_color;
    private double accounts;
    private double registrationFee;

    public ClubForum() {
    }

    public ClubForum(String name) {
        this.name = name;
    }

    public ClubForum(int forumID, String name, String username, String password, String settingsPass, String isRecruitmentOn, String hrPass, String prPass, String rndPass, String treasuryPass, double accounts, double registrationFee) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.settingsPass = settingsPass;
        this.isRecruitmentOn = Boolean.parseBoolean(isRecruitmentOn);
        this.hrPass = hrPass;
        this.prPass = prPass;
        this.rndPass = rndPass;
        this.treasuryPass = treasuryPass;
        this.accounts = accounts;
        this.registrationFee = registrationFee;
    }

    public ClubForum(int forumID, String name, String username, String password, String settingsPass, String isRecruitmentOn, String hrPass, String prPass, String rndPass, String treasuryPass, double accounts) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.settingsPass = settingsPass;
        this.isRecruitmentOn = Boolean.parseBoolean(isRecruitmentOn);
        this.hrPass = hrPass;
        this.prPass = prPass;
        this.rndPass = rndPass;
        this.treasuryPass = treasuryPass;
        this.accounts = accounts;
    }

    public ClubForum(int forumID, String name, String username, String password, String settingsPass, String isRecruitmentOn, String hrPass, String prPass, String rndPass, String treasuryPass, String primary_color, String secondary_color, double accounts) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.settingsPass = settingsPass;
        this.isRecruitmentOn = Boolean.parseBoolean(isRecruitmentOn);
        this.hrPass = hrPass;
        this.prPass = prPass;
        this.rndPass = rndPass;
        this.treasuryPass = treasuryPass;
        this.primary_color = primary_color;
        this.secondary_color = secondary_color;
        this.accounts = accounts;
    }

    public ClubForum(int forumID, String name, String username, String password, String settingsPass, String isRecruitmentOn, String hrPass, String prPass, String rndPass, String treasuryPass, String primary_color, String secondary_color) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.settingsPass = settingsPass;
        this.isRecruitmentOn = Boolean.parseBoolean(isRecruitmentOn);
        this.hrPass = hrPass;
        this.prPass = prPass;
        this.rndPass = rndPass;
        this.treasuryPass = treasuryPass;
        this.primary_color = primary_color;
        this.secondary_color = secondary_color;
    }

    public ClubForum(int forumID, String name, String username, String password, String settingsPass, String isRecruitmentOn) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.settingsPass = settingsPass;
        this.isRecruitmentOn = Boolean.parseBoolean(isRecruitmentOn);
    }


    public int getForumID() {
        return forumID;
    }

    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSettingsPass() {
        return settingsPass;
    }

    public void setSettingsPass(String settingsPass) {
        this.settingsPass = settingsPass;
    }

    public boolean isRecruitmentOn() {
        return isRecruitmentOn;
    }

    public void setRecruitmentOn(boolean recruitmentOn) {
        isRecruitmentOn = recruitmentOn;
    }

    public String getHrPass() {
        return hrPass;
    }

    public String getPrPass() {
        return prPass;
    }

    public String getRndPass() {
        return rndPass;
    }

    public String getTreasuryPass() {
        return treasuryPass;
    }

    public void setHrPass(String hrPass) {
        this.hrPass = hrPass;
    }

    public void setPrPass(String prPass) {
        this.prPass = prPass;
    }

    public void setRndPass(String rndPass) {
        this.rndPass = rndPass;
    }

    public void setTreasuryPass(String treasuryPass) {
        this.treasuryPass = treasuryPass;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public String getSecondary_color() {
        return secondary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public void setSecondary_color(String secondary_color) {
        this.secondary_color = secondary_color;
    }

    public double getAccounts() {
        return accounts;
    }

    public void setAccounts(double accounts) {
        this.accounts = accounts;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }
}
