# Étape de Build
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# 1. Copier le fichier projet depuis le sous-dossier
COPY ["brasil_burger/brasil_burger.csproj", "brasil_burger/"]

# 2. Restaurer les dépendances
RUN dotnet restore "brasil_burger/brasil_burger.csproj"

# 3. Copier tout le reste du code
COPY . .

# 4. Compiler le projet en mode Release
WORKDIR "/src/brasil_burger"
RUN dotnet publish "brasil_burger.csproj" -c Release -o /app/publish

# Étape finale (Exécution)
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app
COPY --from=build /app/publish .

# Configuration du port pour Render (par défaut 10000)
ENV ASPNETCORE_URLS=http://+:10000
EXPOSE 10000

ENTRYPOINT ["dotnet", "brasil_burger.dll"]