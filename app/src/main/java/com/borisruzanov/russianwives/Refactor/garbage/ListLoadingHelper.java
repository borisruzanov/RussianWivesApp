package com.borisruzanov.russianwives.Refactor.garbage;

public class ListLoadingHelper<T> {
    //TODO DELETO OR NOT
    private State state = State.EMPTY;
    private ViewController<T> viewController;

    public ListLoadingHelper(ViewController<T> viewController) {
        this.viewController = viewController;
    }

    public void setState(State state){
        switch (state){
            case LOADING:
                viewController.showLoading(true);
                break;
            case LOADED:
                viewController.showLoading(false);
                break;
            case ERROR:
                viewController.showError();
                break;
        }
        this.state = state;
    }

    public State getState(){
        return state;
    }

    public enum State {
        EMPTY, LOADING, LOADED, ERROR
    }

    public interface ViewController<T>{
        void showLoading(boolean isLoading);
        void showError();
    }

}
