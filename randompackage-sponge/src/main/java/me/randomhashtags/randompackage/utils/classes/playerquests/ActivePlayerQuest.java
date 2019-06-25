package me.randomhashtags.randompackage.utils.classes.playerquests;

public class ActivePlayerQuest {
    private long startedTime;
    private PlayerQuest quest;
    private double progress;
    private boolean completed, claimedRewards;
    public ActivePlayerQuest(long startedTime, PlayerQuest quest, double progress, boolean completed, boolean claimedRewards) {
        this.startedTime = startedTime;
        this.quest = quest;
        this.progress = progress;
        this.completed = completed;
        this.claimedRewards = claimedRewards;
    }
    public long getStartedTime() { return startedTime; }
    public void setStartedTime(long startedTime) { this.startedTime = startedTime; }
    public PlayerQuest getQuest() { return quest; }
    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public boolean hasClaimedRewards() { return claimedRewards; }
    public void setHasClaimedRewards(boolean claimedRewards) { this.claimedRewards = claimedRewards; }
}