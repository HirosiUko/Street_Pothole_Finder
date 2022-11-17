// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

// Area Chart Example
var ctx = document.getElementById("myAreaChart");
var myLineChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: ["Nov 1", "Nov 2", "Nov 3", "Nov 4", "Nov 5", "Nov 6", "Nov 7"],
    datasets: [
      {
      label: "Crack 크랙",
      lineTension: 0.3,
      backgroundColor: "rgba(2,117,216,0.2)",
      borderColor: "rgba(2,117,216,1)",
      pointRadius: 5,
      pointBackgroundColor: "rgba(2,117,216,1)",
      pointBorderColor: "rgba(255,255,255,0.8)",
      pointHoverRadius: 5,
      pointHoverBackgroundColor: "rgba(2,117,216,1)",
      pointHitRadius: 50,
      pointBorderWidth: 2,
      data: [186, 386, 719, 100, 98, 32, 813],
    },{
      label: "Pothole 포트홀",
      lineTension: 0.3,
      backgroundColor: "rgba(220,53,69,0.69)",
      borderColor: "rgba(220,53,69,1)",
      pointRadius: 5,
      pointBackgroundColor: "rgba(220,53,69,0.69)",
      pointBorderColor: "rgba(220,53,69,0.8)",
      pointHoverRadius: 5,
      pointHoverBackgroundColor: "rgba(220,53,69,0.69)",
      pointHitRadius: 50,
      pointBorderWidth: 2,
      data: [500, 800, 600, 100, 32, 46, 186, 100],
    }],
  },
  options: {
    scales: {
      xAxes: [{
        time: {
          unit: 'date'
        },
        gridLines: {
          display: false
        },
        ticks: {
          maxTicksLimit: 7
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 2000,
          maxTicksLimit: 5
        },
        gridLines: {
          color: "rgba(0, 0, 0, .125)",
        }
      }],
    },
    legend: {
      display: true
    }
  }
});
