import yfinance as yf
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def get_gold_data(period='2y'):
    """Fetch Gold futures data."""
    print("Fetching Gold futures data...")
    gold_ticker = yf.Ticker("GC=F")
    df = gold_ticker.history(period=period)
    return df

def calculate_moving_averages(df, short_window=50, long_window=200):
    """Calculate short and long simple moving averages."""
    df['SMA50'] = df['Close'].rolling(window=short_window).mean()
    df['SMA200'] = df['Close'].rolling(window=long_window).mean()
    return df

def generate_signals(df):
    """Generate buy and sell signals based on moving average crossovers."""
    df['Signal'] = 0.0

    # Generate signal when SMA50 crosses above SMA200 (Buy)
    # Generate signal when SMA50 crosses below SMA200 (Sell)

    # We only care about signals after both SMAs are calculated
    mask = df['SMA200'].notna()

    # 1 for buy, -1 for sell
    df.loc[mask, 'Signal'] = np.where(df.loc[mask, 'SMA50'] > df.loc[mask, 'SMA200'], 1.0, -1.0)

    # Find points of crossover to mark actual trades
    df['Position'] = df['Signal'].diff()

    return df

def plot_data(df):
    """Plot the closing price, moving averages, and signals."""
    print("Generating moving average chart...")
    plt.figure(figsize=(14, 8))

    # Plot closing price and SMAs
    plt.plot(df.index, df['Close'], label='Gold Price (GC=F)', alpha=0.5, color='gray')
    plt.plot(df.index, df['SMA50'], label='50-day SMA', alpha=0.9, color='blue')
    plt.plot(df.index, df['SMA200'], label='200-day SMA', alpha=0.9, color='red')

    # Plot buy signals (Position == 2.0 because signal goes from -1 to 1)
    buy_signals = df[df['Position'] == 2.0]
    plt.scatter(buy_signals.index, buy_signals['SMA50'],
                label='Buy Signal', marker='^', color='green', s=150)

    # Plot sell signals (Position == -2.0 because signal goes from 1 to -1)
    sell_signals = df[df['Position'] == -2.0]
    plt.scatter(sell_signals.index, sell_signals['SMA50'],
                label='Sell Signal', marker='v', color='red', s=150)

    plt.title('Gold Price - 50-day and 200-day Moving Average Crossover Strategy')
    plt.xlabel('Date')
    plt.ylabel('Price (USD)')
    plt.legend(loc='best')
    plt.grid(True, alpha=0.3)

    # Save the plot
    plt.savefig('gold_trading_chart.png')
    print("Chart saved to 'gold_trading_chart.png'")

def get_latest_suggestion(df):
    """Output the latest trading suggestion based on current state."""
    latest = df.iloc[-1]

    print("\n--- LATEST GOLD TRADING SUGGESTION ---")
    print(f"Date: {df.index[-1].strftime('%Y-%m-%d')}")
    print(f"Current Price: ${latest['Close']:.2f}")

    if pd.isna(latest['SMA50']) or pd.isna(latest['SMA200']):
         print("Not enough data to calculate moving averages.")
         return

    print(f"50-day SMA: ${latest['SMA50']:.2f}")
    print(f"200-day SMA: ${latest['SMA200']:.2f}")

    if latest['Position'] == 2.0:
        print("SIGNAL: BUY (Golden Cross detected!)")
    elif latest['Position'] == -2.0:
        print("SIGNAL: SELL (Death Cross detected!)")
    else:
        if latest['SMA50'] > latest['SMA200']:
            print("STATUS: HOLD (Uptrend - 50-day SMA is above 200-day SMA)")
        else:
            print("STATUS: AVOID / SHORT (Downtrend - 50-day SMA is below 200-day SMA)")
    print("--------------------------------------\n")

if __name__ == "__main__":
    df = get_gold_data()
    df = calculate_moving_averages(df)
    df = generate_signals(df)
    plot_data(df)
    get_latest_suggestion(df)
