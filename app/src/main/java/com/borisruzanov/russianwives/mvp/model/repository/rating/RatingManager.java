package com.borisruzanov.russianwives.mvp.model.repository.rating;

/**
 * Class which manage rating system
 */
public class RatingManager {

    private static RatingManager instance;

    public static final int USER_MUST_INFO = 5;
    private static final int USER_LIKE_POINTS = 1;
    private static final int USER_MSG_POINTS = 1;
    private static final int USER_FULL_PROFILE = 8;
    private static final int FRIEND_LIKE_POINTS = 2;
    private static final int FRIEND_VISIT_POINTS = 2;

    private RatingRepository ratingRepository = new RatingRepository();

    public static RatingManager getInstance() {
        if (instance == null) {
            instance = new RatingManager();
        }
        return instance;
    }

    /**
     * Increase rating when user put likes on someone account
     */
    public void setUserLikePts(){
        ratingRepository.addRating(USER_LIKE_POINTS);
    }

    /**
     * Increase rating when user send message
     */
    public void setUserMsgPts(){
        ratingRepository.addRating(USER_MSG_POINTS);
    }

    /**
     * Increase rating when user make full profile achieve
     */
    public void setUserFullProfile(){
        ratingRepository.addRating(USER_FULL_PROFILE);
    }

    /**
     * Increase rating of the user when somebody like his profile
     */
    public void setFriendLikePts(){
        ratingRepository.addRating(FRIEND_LIKE_POINTS);
    }

    /**
     * Increase rating of the user when somebody visit his profile
     */
    public void setFriendVisitPts(){
        ratingRepository.addRating(FRIEND_VISIT_POINTS);
    }
}
