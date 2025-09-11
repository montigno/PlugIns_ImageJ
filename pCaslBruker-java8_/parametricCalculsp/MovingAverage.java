package parametricCalculsp;

public class MovingAverage {
  private double sum = 0.0D;
  
  private double[] y_av;
  
  private double[][] max_min = new double[2][2];
  
  public MovingAverage(double[] yoff, int n) {
    this.y_av = new double[yoff.length];
    int a = n / 2;
    for (int j = 0; j < yoff.length; j++) {
      for (int i = 0; i < n; i++) {
        if (j + i - a < 0) {
          this.sum += yoff[j + i - a + yoff.length];
        } else if (j + i - a >= yoff.length) {
          this.sum += yoff[j + i - a - yoff.length];
        } else {
          this.sum += yoff[j + i - a];
        } 
      } 
      this.y_av[j] = this.sum / n;
      if (j == 0) {
        this.max_min[0][1] = this.y_av[j];
        this.max_min[0][0] = 0.0D;
        this.max_min[1][1] = this.y_av[j];
        this.max_min[1][0] = 0.0D;
      } else {
        if (this.y_av[j] > this.max_min[0][1]) {
          this.max_min[0][1] = this.y_av[j];
          this.max_min[0][0] = j;
        } 
        if (this.y_av[j] < this.max_min[1][1]) {
          this.max_min[1][1] = this.y_av[j];
          this.max_min[1][0] = j;
        } 
      } 
      this.sum = 0.0D;
    } 
  }
  
  public double[] getAvMo() {
    return this.y_av;
  }
  
  public double[][] getMaxMin() {
    return this.max_min;
  }
}