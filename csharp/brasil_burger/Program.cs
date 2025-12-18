using Microsoft.EntityFrameworkCore;
using brasil_burger.Data;

var builder = WebApplication.CreateBuilder(args);

// Configuration de la connexion Neon
var connectionString = "Host=ep-sparkling-sea-agx6nlwz-pooler.c-2.eu-central-1.aws.neon.tech;Database=neondb;Username=neondb_owner;Password=npg_Uqao7VzLZRy3;SSL Mode=Require;Trust Server Certificate=true";

builder.Services.AddDbContext<BrasilBurgerContext>(options =>
    options.UseNpgsql(connectionString));

builder.Services.AddControllersWithViews();

var app = builder.Build();

if (!app.Environment.IsDevelopment()) {
    app.UseExceptionHandler("/Home/Error");
}

app.UseStaticFiles();
app.UseRouting();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Catalogue}/{action=Index}/{id?}"); // Catalogue par défaut

app.Run();