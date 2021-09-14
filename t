به نام خدا

پروژه شامل دو پکیج می باشد که هر کدام شامل کلاس های مربوطه است. برای پیاده سازی جهان، یک کلاس به نام house ایجاد کردم که هر شی از آن، خانه ای از جهان را به وجود می آورد. آرایه دوبعدی از آن، جهان را تشکیل میدهد.
برای پیاده سازی به صورت ترد، کلاس animal که ترد را اکستند میکند به صورت زیرکلاسی از کلاس اصلی تعریف شده است و مسئولیت بخش زندگی را برعهده دارد. در تابع main کلاس اصلی، یک تایمر است که هر یک ثانیه وظایف مربوط به تولد و مرگ را انجام میدهد. از آنجایی که مرگ در پایان هر واحد زمانی رخ میدهد، آن را در ابتدای واحد زمانی بعدی پیاده سازی کردم زیرا در حالتی که انتهای هر واحد زمانی انجام میشود، با تلاش های بسیار نتوانستم بدون همزمانی با حرکت ترد ها آن را پیاده سازی کنم. بنابراین، عمل مرگ مربوط به هر واحد زمانی، در ابتدای واحد زمانی بعد صورت میپذیرد.
برای هر خانه، همسایه هایش را با استفاده از تابع SetHouseNeighbors  تعیین کرده و از نتایج آن برای قسمت حرکت و مرگ استفاده کردم.
برای پیاده سازی synchronization از سمافور استفاده کرده ام و آن را قبل و بعد از هر بخش wait و سپس signal نموده ام.
برای تعیین کردن تمام خانه هایی که شامل موجودات با نوع یکسان هستند، یک map به نام eachidlocations تعریف کرده ام که برای هر آی دی، خانه هایی که موجودات با آن آی دی را دارند در یک لیست نگه میدارد.
در پیاده سازی به شکل پروسس نیز دقیقا از تابع های حالت ترد استفاده کرده ام با این تفاوت که تمام کار ها را کلاس اصلی انجام میدهد و فقط در بخش حرکت، پردازه مربوطه به وسیله ارتباط مستقیم با کلاس اصلی( از طریق system.out) عدد تصادفی مربوط به خانه مقصد را به کلاس اصلی ارسال میکند.

به طور کلی در حالتی که تبصری ۱و۲ پیاده سازی شوند یا نه، موجودات قوی تر باقی میمانند.

پارامتر های ورودی دقیقا با نام های گفته شده در داک هستند و ابتدای ورودی ها باید به وسیله true یا false استفاده از تبصره ها یا عدم استفاده از آن را مشخص کرد.