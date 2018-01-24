package net.ddns.endercrypt.library.booleanlock;

/**
 * a simple multi thread class which allows locking and unlocking
 * @author EnderCrypt
 */
public class BooleanLock
{
	private volatile boolean locked;

	private final Object lockObject = new Object();

	public BooleanLock()
	{
		this(false);
	}

	public BooleanLock(boolean locked)
	{
		this.locked = locked;
	}

	/**
	 * locks the lock, does nothing if its already locked
	 */
	public void lock()
	{
		synchronized (lockObject)
		{
			locked = true;
		}
	}

	/**
	 * unlocks the lock if its locked, otherwise does nothing
	 */
	public void unlock()
	{
		synchronized (lockObject)
		{
			if (locked)
			{
				locked = false;
				lockObject.notifyAll();
			}
		}
	}

	/**
	 * returns true or false depending on whether the lock is locked or not
	 * @return if locked
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * will block the current thread untill the lock gets unlocked (by another thread)
	 * @throws InterruptedException if the thread got interrupted
	 */
	public void waitForUnlocked() throws InterruptedException
	{
		synchronized (lockObject)
		{
			while (locked)
			{
				lockObject.wait();
			}
		}
	}

	/**
	 * behaves the same as waitForUnlocked() but the moment the lock gets unlocked, this method will re-lock it instantly
	 * @throws InterruptedException
	 * @see BooleanLock#waitForUnlocked
	 */
	public void waitForUnlockedThenLock() throws InterruptedException
	{
		synchronized (lockObject)
		{
			waitForUnlocked();
			lock();
		}
	}
}
