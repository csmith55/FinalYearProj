package app.com.project.csmith.finalyearproject.Places;

/**
 * Used to store all the information regarding reviews
 */
public class Reviews {
    private String author;
    private double rating;
    private String text;

    public Reviews(String author, double rating, String text) {
        this.author = author;
        this.rating = rating;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
